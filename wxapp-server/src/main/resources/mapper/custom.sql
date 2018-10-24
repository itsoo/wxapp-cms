#sql("queryList")
  select id, name, phone, address from t_custom
    where (name like concat('%', #para(0), '%')
      or phone like concat('%', #para(0), '%')
      or address like concat('%', #para(0), '%'))
    and user = #para(1)
#end

#sql("updateGiftCount")
  update t_buy_history set free = '1'
    where free = '0'
    and custom = #para(0)
  order by buy_date, id
  limit 10
#end
