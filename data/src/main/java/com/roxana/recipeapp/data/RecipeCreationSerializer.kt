package com.roxana.recipeapp.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RecipeCreationSerializer : Serializer<RecipeCreation> {
    override val defaultValue: RecipeCreation = RecipeCreation.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RecipeCreation {
        try {
            return RecipeCreation.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read recipe proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: RecipeCreation,
        output: OutputStream
    ) = t.writeTo(output)
}
