package ai.lingopulse.domain.agent.usecase

import ai.lingopulse.domain.agent.mapper.toFeedDetails
import ai.lingopulse.domain.common.model.FeedDetails
import ai.lingopulse.domain.agent.model.feed.model.FeedDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetFeedDetailsUseCase {
    operator fun invoke(
        feedId: String
    ): Flow<FeedDetails>
}

class GetFeedDetailsUseCaseImpl() : GetFeedDetailsUseCase {

    override operator fun invoke(
        feedId: String
    ): Flow<FeedDetails> = flow {
        emit(
            FeedDetailsResponse(
                id = "4d9f79ecae34ba815f222a0e1285736b683ceb34dfceb3d1311b5de61da77de6",
                category = "technology",
                title = "Dbrand’s Killswitch is the best all-around Switch 2 case - The Verge",
                subtitle = "Dbrand’s Killswitch for the Switch 2 isn’t cheap, starting at \$59.99 and going up for more protection. But it’s currently the best there is when it comes to fit.",
                author = "Cameron Faulkner",
                imageUrl = "https://platform.theverge.com/wp-content/uploads/sites/2/2025/06/ks2front.jpg?quality=90&strip=all&crop=0%2C15.015562043091%2C100%2C69.968875913818&w=1200",
                publishedAt = "2025-06-13T15:24:47Z",
                content = "The \$79.85 “Travel” tier offers complete protection, but even the \$59.95 version is great.\n\nThe \$79.85 “Travel” tier offers complete protection, but even the \$59.95 version is great.\n\nIf you buy something from a Verge link, Vox Media may earn a commission.See our ethics statement.\n\nI’ve been checking out a lot of new Switch 2 accessories recently, almost all of which were produced before the companies that made them ever touched a real Switch 2. Even a millimeter’s difference in dimensions could completely throw off a design, and some products I’ve tried lack a perfect fit. For example, Genki’s Attack Vectorhas problems, which the company is now reworking. It’s probably not the only one. However, Dbrand’s Killswitch kit offers the best fit and best protection right out of the gate.\n\nI adored the kit when I tested it on mySteam Deck, and this one doesn’t mess with the winning formula. Dbrand had to engineer its own USB-C dock adapter since a Killswitch-equipped console doesn’t fit in Nintendo’s dock. Its adapter is included, with no cutbacks to visual fidelity in docked mode. It’s a little clunky, but I’m mainly just happy that it works, so I don’t have to take the Killswitch off every time I want to dock the Switch 2.\n\nThe most basic “Essentials” kit is\$59.95and includes a grippy, form-fitting shell that latches around the Switch 2’s back, slightly latching around the front. It can be secured to your console with some small adhesive strips. A separate, adhesive-backed strip of protection for the kickstand comes with the kit, too. Dbrand includes two Joy-Con 2 shells that have thick, textured grips that make them significantly more comfortable to use without adding too much weight. Unlike Joy-Con shells from other accessory makers, these were as easy to install as I hoped they would be, sliding securely into place thanks to the Killswitch’s tough, but just-malleable-enough materials.\n\nThe\$79.85“Travel” option adds a tough latch-on cover that protects the front of your Switch 2, and a tray that fits within it for storing up to 10 game cartridges. Of the cases I’ve tried so far, I have the fewest reservations about putting my Switch 2 in this one. With the front cover on, it provides hard case protection. When detached, it offers solid protection with an understated look for the parts of the Switch 2 that are most vulnerable to damage.\n\nThe most expensive\$99.80“Ultra” kit includes everything mentioned above, plus additional stick grips and two glass screen protectors. The added cost isn’t outrageous if you still need a screen protector.\n\nThe Killswitch is great. I often forget that it’s installed. I thought I’d miss the console’s slim design, arguably one of its biggest selling points compared to chunkier handhelds like the Steam Deck. But, after mainly using a Deck instead of the aging Switch, I’m spoiled by big grips, and the Killswitch turns the Switch 2 into something that I want to use in handheld mode.\n\nA weekly newsletter by David Pierce designed to tell you everything you need to download, watch, read, listen to, and explore that fits in The Verge’s universe.",
                url = "https://www.theverge.com/tech/686719/dbrand-killswitch-switch-2-case-review"
            ).toFeedDetails()
        )
    }
}