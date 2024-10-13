    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //判断菜品是否要改为停售
        if(status == StatusConstant.DISABLE) {
            //判断该菜品是否有关联的套餐
            List<Long> ids = new ArrayList<>();
            ids.add(id);
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
            if(setmealIds != null && setmealIds.size() > 0) {
                setmealIds.forEach(setmealId -> {
                    Setmeal setmeal = setmealMapper.getById(setmealId);
                    //判断这些套餐是否有在售的
                    if(setmeal.getStatus() == StatusConstant.ENABLE) {
                        throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL2); //自定义的无法停售提示
                    }
                });
            }
        }

        Dish dish = Dish.builder().id(id)
                .status(status).build();
        dishMapper.update(dish);
    }
