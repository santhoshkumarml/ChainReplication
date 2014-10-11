package async.chainreplication.server.models;

public interface IApplicationModel<K,V> {
	void set(K key, V value);
}
