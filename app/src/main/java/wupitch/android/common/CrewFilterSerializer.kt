package wupitch.android.common

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.wupitch.android.CrewFilter
import java.io.InputStream
import java.io.OutputStream


object CrewFilterSerializer : Serializer<CrewFilter> {
    override val defaultValue: CrewFilter = CrewFilter.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CrewFilter {
        try {
            return CrewFilter.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: CrewFilter,
        output: OutputStream
    ) = t.writeTo(output)
}