package com.mayifan.service;

import com.mayifan.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Mark
* @description 针对表【address_book(地址管理)】的数据库操作Service
* @createDate 2022-09-20 20:08:26
*/
public interface AddressBookService extends IService<AddressBook> {

    void updateDefaultAddress(AddressBook addressBook);
}
