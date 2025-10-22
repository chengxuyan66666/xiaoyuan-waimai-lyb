package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;

import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(DishDTO dishDTO) {
        Dish dish=new Dish();
        //属性拷贝
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        Long id = dish.getId();
        List<DishFlavor> flavors =dishDTO.getFlavors();
      if(!flavors.isEmpty()) {
          flavors.forEach(flavor->
          {
              flavor.setDishId(id);
          });
          dishFlavorMapper.insert(flavors);
      }
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> p=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    public DishVO findById(Long id) {
        Dish dish=dishMapper.selectById(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        log.info("dish{},dishVO{}",dish,dishVO);
       List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);
       log.info("查询菜品口味数据{}",dishFlavors);
       if(!dishFlavors.isEmpty()) {
           dishVO.setFlavors(dishFlavors);
       }
       log.info("dishVO{}",dishVO);
       return dishVO;
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delect(List<Long> ids) {
        //判断是否停售
        for (Long id : ids) {
            Dish dish=dishMapper.selectById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                log.info("有菜品{}正在销售中,不能删除",dish);
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断是否关联套餐
       List<Long> setmealIds=setmealDishMapper.selectIdsByDishIds(ids);
        if(!setmealIds.isEmpty()){
            log.info("套餐中包含要删除的菜品,套餐id:{}",setmealIds);
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品数据
        dishMapper.delectByIds(ids);
        //删除菜品口味数据
        dishFlavorMapper.delectByDishIds(ids);

    }

    /**
     * 修改菜品和菜品口味数据
     * @param dishDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeWithFlavor(DishDTO dishDTO) {
        //1.修改菜品
        //属性拷贝 原始dish才有创建和修改属性
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //2.删除原有菜品口味数据
        dishFlavorMapper.delectByDishId(dishDTO.getId());
        //3添加新的菜品口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(!flavors.isEmpty()){
            flavors.forEach(flavor->{
                flavor.setDishId(dish.getId());
            });
            log.info("f{}",flavors);
            dishFlavorMapper.insert(flavors);
        }
    }

    /**
     * 起售停售菜品
     * @param status
     * @param id
     */
    public void stopOrStart(Integer status, Long id) {
        Dish dish =new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> findByCategoryId(Long categoryId) {
        return dishMapper.selectByCategoryId(categoryId);
    }
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.select(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
