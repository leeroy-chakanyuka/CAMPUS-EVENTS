package za.ac.cput.campus_events.service;

public interface Iservice<T, ID> {

    T create(T t);
    T read(ID id);
    T update(T t);
    void delete(ID id);
}
