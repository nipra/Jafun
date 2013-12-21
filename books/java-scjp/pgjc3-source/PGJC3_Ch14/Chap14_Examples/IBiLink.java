interface IBiLink<T> extends IMonoLink<T> {
  void    setPrevious(IBiLink<T> previous);
  IBiLink<T> getPrevious();
}