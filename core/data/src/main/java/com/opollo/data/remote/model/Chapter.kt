package com.opollo.data.remote.model

import kotlinx.serialization.Serializable
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Serializable
data class Chapter(
    val title:String,
    val audioUrl:String,
    val duration:String,
    val chapterNumber:Int
)

@Root(name = "rss", strict = false)
data class RssFeed(
    @field:Path("channel")
    @field:ElementList("item", inline = true)
    var items:List<RssItem> = mutableListOf()
)

@Root(name = "item", strict = false)
data class RssItem(
    @field:Element("title")
    var title:String = "",
    @field:Path("enclosure")
    @field:Attribute("url")
    var audioUrl: String = "",
    @field:Element("duration")
    @field:Namespace(prefix = "itunes")
    var duration: String = "00:00:00",
    @field:Element(name = "episode")
    @field:Namespace(prefix = "itunes")
    var episodeNumber:Int = 0
)