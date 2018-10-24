#sql("query")
  select * from t_user
  #if(name != null && !"".equals(name))
    where name like concat('%', ?, '%')
  #end
#end
