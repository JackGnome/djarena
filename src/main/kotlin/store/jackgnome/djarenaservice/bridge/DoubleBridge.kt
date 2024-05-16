package store.jackgnome.djarenaservice.bridge

import org.hibernate.search.mapper.pojo.bridge.ValueBridge
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext

class DoubleBridge : ValueBridge<Double?, String> {

    override fun toIndexedValue(value: Double?, context: ValueBridgeToIndexedValueContext?): String? {
        return value?.toString()
    }
}