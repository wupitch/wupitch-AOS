package wupitch.android.common

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.wupitch.android.ImpromptuFilter
import java.io.InputStream
import java.io.OutputStream


object ImpromptuFilterSerializer : Serializer<ImpromptuFilter> {
    override val defaultValue: ImpromptuFilter = ImpromptuFilter.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ImpromptuFilter {
        try {
            return ImpromptuFilter.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: ImpromptuFilter,
        output: OutputStream
    ) = t.writeTo(output)
}