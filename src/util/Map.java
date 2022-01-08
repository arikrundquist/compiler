
package util;

public final class Map {

    private Map() { }

    public static <K, V> V get(java.util.Map<K, V> map, K key, V _default) {
        V val = map.get(key);
        if(val != null) {
            return val;
        }
        map.put(key, _default);
        return _default;
    }
}
