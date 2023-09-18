import kotlinx.serialization.Serializable
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import java.io.StringReader

@Serializable
sealed interface SomeThing

@Serializable
data class Token(val token: String, val original: String): SomeThing

@Serializable
@JvmInline
value class Text(val t: String): SomeThing

fun tokenizeEnglishText(text: String): MutableList<SomeThing> {
    val analyzer = EnglishAnalyzer()
    val tokenStream = analyzer.tokenStream(null, StringReader(text))
    val termAttr = tokenStream.addAttribute(CharTermAttribute::class.java)
    val offsetAttr = tokenStream.addAttribute(OffsetAttribute::class.java)

    val tokens = mutableListOf<SomeThing>()
    tokenStream.reset()

    var previousPosition = 0
    while (tokenStream.incrementToken()) {
        if (offsetAttr.startOffset() != previousPosition) {
            tokens += Text(text.substring(previousPosition, offsetAttr.startOffset()))
        }
        tokens += Token(termAttr.toString(), text.substring(offsetAttr.startOffset(), offsetAttr.endOffset()))
        previousPosition = offsetAttr.endOffset()
    }
    tokens += Text(text.substring(previousPosition, text.length))

    tokens += Text("Hello 4")

        tokenStream.end()
    tokenStream.close()

    return tokens
}