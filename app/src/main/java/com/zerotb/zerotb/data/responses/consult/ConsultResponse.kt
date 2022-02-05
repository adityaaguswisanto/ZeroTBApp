package com.zerotb.zerotb.data.responses.consult

import com.zerotb.zerotb.data.responses.data.Consult
import com.zerotb.zerotb.data.responses.link.Link

data class ConsultResponse(
    val current_page: Int,
    val `data`: List<Consult>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class BookResponse(
    val message: String
)