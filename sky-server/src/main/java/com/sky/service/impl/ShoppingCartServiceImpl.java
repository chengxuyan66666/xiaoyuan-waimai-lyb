package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断购物车中是否存在要添加的
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateById(cart);
        } else {
            //购物车中没有，则添加
            Long dishId = shoppingCart.getDishId();
            if (dishId != null) {
                //添加菜品
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                Setmeal setmeal = setmealMapper.selectById2(shoppingCart.getSetmealId());
                //添加套餐
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        //获取当前顾客id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void delect() {
        ShoppingCart shoppingCart = new ShoppingCart();
        //获取当前顾客id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.delect(shoppingCart);

    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        //拷贝属性
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //获取当前顾客id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //应该先查询当前项目在购物车的数量
         List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list!=null &&list.size()>0){
             ShoppingCart cart = list.get(0);
             if(cart.getNumber()==1){
            shoppingCartMapper.delect(shoppingCart);}
             else {
                 cart.setNumber(cart.getNumber()-1);
                 shoppingCartMapper.updateById(cart);
             }
        }


    }
}
