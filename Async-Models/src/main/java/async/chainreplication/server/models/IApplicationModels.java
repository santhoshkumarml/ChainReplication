package async.chainreplication.server.models;

public interface IApplicationModels<K,V> {
	void addNew(IApplicationModel<K, V> model);
	IApplicationModel get(K key);
}
