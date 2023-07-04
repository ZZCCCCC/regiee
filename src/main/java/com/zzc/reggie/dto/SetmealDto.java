package com.zzc.reggie.dto;

import com.zzc.reggie.entity.Setmeal;
import com.zzc.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
