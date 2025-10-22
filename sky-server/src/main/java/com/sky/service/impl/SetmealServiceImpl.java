package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        //拷贝属性
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //新增套餐默认停售
        setmeal.setStatus(StatusConstant.DISABLE);
        //添加套餐信息
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        //在套餐中添加菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (!setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish->
            {
                dish.setSetmealId(setmealId);
            });
            setmealDishMapper.insert(setmealDishes);
        }
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> p=setmealMapper.pageQuery(setmealPageQueryDTO);
        return  new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        //获取套餐状态
        Integer count=setmealMapper.countStatsu(ids);
        //如果在售卖中抛异常
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        //删除套餐信息
        setmealMapper.delete(ids);
        //删除套餐中包含的菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    public SetmealVO findById(Long id) {
        //先查询套餐信息
        SetmealVO setmealVO=setmealMapper.selectById(id);
        //根据id查询套餐菜品
        List<SetmealDish> setmealDishes=setmealDishMapper.selectBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SetmealDTO setmealDTO) {
        //拷贝属性
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //1.修改套餐信息
        setmealMapper.update(setmeal);
        //2.删除套餐菜品
         Long setmealId = setmeal.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        //重新插入套餐菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(!setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish -> {
                dish.setSetmealId(setmealId);
            });
            setmealDishMapper.insert(setmealDishes);
        }

    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    /**
     * 起售停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
         Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
         setmealMapper.update(setmeal);
    }
}
