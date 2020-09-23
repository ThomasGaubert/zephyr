package com.texasgamer.zephyr.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Represents item in "What's new" list.
 */
class WhatsNewItem(@field:DrawableRes @get:DrawableRes
                   @param:DrawableRes val iconRes: Int, @field:StringRes @get:StringRes
                   @param:StringRes val titleRes: Int, @field:StringRes @get:StringRes
                   @param:StringRes val bodyRes: Int)