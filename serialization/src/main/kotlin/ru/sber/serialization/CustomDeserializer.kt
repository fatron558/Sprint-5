package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class CustomDeserializer : StdDeserializer<Client7>(Client7::class.java) {
    override fun deserialize(jsonParser: JsonParser?, context: DeserializationContext?): Client7 {
        val node: JsonNode = jsonParser!!.codec.readTree(jsonParser)
        val dataClientName = node.get("client").asText().split(" ")
        return Client7(dataClientName[1], dataClientName[0], dataClientName[2])
    }
}