package com.zerotb.zero.data.responses.pill

import com.zerotb.zero.data.responses.data.Pill
import com.zerotb.zero.data.responses.data.Result
import com.zerotb.zero.data.responses.link.Link

data class PillResponse(
    val current_page: Int,
    val `data`: List<Pill>,
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

data class DrugResponse(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)