interface IMonoLink<E> {
  void     setData(E data);
  E        getData();
  void     setNext(IMonoLink<E> next);
  IMonoLink<E> getNext();
}