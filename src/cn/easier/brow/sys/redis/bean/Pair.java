package cn.easier.brow.sys.redis.bean;

/**
 * 键值对
 * @version V1.0
 * @author fengjc
 * @param <K> key
 * @param <V> value
 */
public class Pair<K, V> {

	private K key;
	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 构造Pair键值对
	 * @param key key
	 * @param value value
	 * @return 键值对
	 */
	@SuppressWarnings("hiding")
	public <K, V> Pair<K, V> makePair(K key, V value) {
		return new Pair<K, V>(key, value);
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}