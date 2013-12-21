package com.practicalHadoop.emr.builders;

import java.util.List;

public abstract class BaseListBuilder<TEntity> extends BaseBuilder<List<TEntity>> {

	protected abstract TEntity build(int index);
	
	protected void buildList(List<TEntity> result)
    {
		int index = 1;
		while(true)
		{
			TEntity entity = this.build(index);
			if (entity == null) 
			{
				break;
			}
			
			result.add(entity);
			index++;
		}
    }
}
