package com.mayifan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.common.BaseContext;
import com.mayifan.pojo.AddressBook;
import com.mayifan.service.AddressBookService;
import com.mayifan.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Mark
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-09-20 20:08:26
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

    @Transactional
    @Override
    public void updateDefaultAddress(AddressBook default_new) {
        //找到默认的地址，改为非默认的
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>().eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, 1);
        AddressBook defaultAddress = this.getOne(wrapper);
        if (defaultAddress!=null) {
            defaultAddress.setIsDefault(0);
            this.updateById(defaultAddress);
        }
        //将新的地址id设为默认的
        default_new.setIsDefault(1);
        this.updateById(default_new);
    }
}




