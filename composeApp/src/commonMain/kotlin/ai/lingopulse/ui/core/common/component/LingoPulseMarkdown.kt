package ai.lingopulse.ui.core.common.component

import ai.lingopulse.ui.core.theme.FontSizeMedium
import ai.lingopulse.ui.core.theme.PaddingXXLarge
import ai.lingopulse.util.extension.isWeb
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.MarkdownSuccess
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import com.mikepenz.markdown.m2.markdownTypography
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.MarkdownAnimations
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownDimens
import com.mikepenz.markdown.model.MarkdownExtendedSpans
import com.mikepenz.markdown.model.MarkdownInlineContent
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.NoOpImageTransformerImpl
import com.mikepenz.markdown.model.ReferenceLinkHandler
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownDimens
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.markdownInlineContent
import com.mikepenz.markdown.model.markdownPadding
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.kodein.emoji.compose.WithPlatformEmoji

@Composable
public fun LingoPulseMarkdown(
    content: String,
    colors: MarkdownColors,
    typography: MarkdownTypography = markdownTypography(),
    modifier: Modifier = Modifier.fillMaxSize(),
    padding: MarkdownPadding = markdownPadding(),
    dimens: MarkdownDimens = markdownDimens(),
    flavour: MarkdownFlavourDescriptor = GFMFlavourDescriptor(),
    parser: MarkdownParser = MarkdownParser(flavour),
    imageTransformer: ImageTransformer = NoOpImageTransformerImpl(),
    annotator: MarkdownAnnotator = markdownAnnotator(),
    extendedSpans: MarkdownExtendedSpans = markdownExtendedSpans(),
    inlineContent: MarkdownInlineContent = markdownInlineContent(),
    components: MarkdownComponents = markdownComponents(),
    animations: MarkdownAnimations = markdownAnimations(),
    referenceLinkHandler: ReferenceLinkHandler = ReferenceLinkHandlerImpl(),
    loading: @Composable (Modifier) -> Unit = { Box(it) },
    success: @Composable (State.Success, MarkdownComponents, Modifier) -> Unit = { state, components, modifier ->
        MarkdownSuccess(
            state,
            components,
            modifier
        )
    },
    error: @Composable (Modifier) -> Unit = { Box(it) }
) {
    if (isWeb()) {
        WithPlatformEmoji(
            text = content,
        ) { emojiAnnotatedString, emojiInlineContent ->
            MarkdownBasicText(
                text = emojiAnnotatedString,
                color = colors.text,
                style = typography.text,
                fontSize = FontSizeMedium,
                inlineContent = emojiInlineContent,
            )
        }

    } else {

        Markdown(
            content = content,
            colors = colors,
            typography = markdownTypography(
                text = TextStyle(fontSize = FontSizeMedium, lineHeight = PaddingXXLarge.value.sp),
                paragraph = TextStyle(fontSize = FontSizeMedium, lineHeight = PaddingXXLarge.value.sp),
                ordered = TextStyle(fontSize = FontSizeMedium, lineHeight = PaddingXXLarge.value.sp),
                bullet = TextStyle(fontSize = FontSizeMedium, lineHeight = PaddingXXLarge.value.sp),
                list = TextStyle(fontSize = FontSizeMedium, lineHeight = PaddingXXLarge.value.sp),
                link = TextStyle(fontSize = FontSizeMedium, lineHeight = PaddingXXLarge.value.sp),
                textLink = TextLinkStyles(
                    style = TextStyle(
                        fontSize = FontSizeMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                        lineHeight = PaddingXXLarge.value.sp
                    ).toSpanStyle()
                )
            ),
            modifier = modifier,
            padding = padding,
            dimens = dimens,
            flavour = flavour,
            parser = parser,
            imageTransformer = imageTransformer,
            annotator = annotator,
            extendedSpans = extendedSpans,
            inlineContent = inlineContent,
            components = components,
            animations = animations,
            referenceLinkHandler = referenceLinkHandler,
            loading = loading,
            success = success,
            error = error
        )
    }
}