package org.kubernetes.hellokube.kafka

import org.kubernetes.hellokube.database.MobileDataService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MobileDataKafkaProcessor(
    private val mobileDataService: MobileDataService
) {
    @KafkaListener(topics = ["\${app.kafka.mobile-data-topic}"])
    fun process(payload: String) {
        mobileDataService.saveMobileData(payload)
    }
}