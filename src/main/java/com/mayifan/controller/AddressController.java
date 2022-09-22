package com.mayifan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mayifan.common.BaseContext;
import com.mayifan.common.R;
import com.mayifan.pojo.AddressBook;
import com.mayifan.service.AddressBookService;
import com.sun.jndi.cosnaming.IiopUrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressController {

    @Resource
    private AddressBookService addressBookService;

    @RequestMapping("/list")
    public R<List<AddressBook>> list() {
        List<AddressBook> list_address = addressBookService.list(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId()));
        return R.success(list_address);
    }

    @PostMapping
    public R<String> addAddress(@RequestBody AddressBook addressBook){
        addressBookService.save(addressBook);
        return R.success("添加成功");
    }

    @PutMapping("/default")
    public R<String> defaultAddress(@RequestBody AddressBook addressBook) {
        addressBookService.updateDefaultAddress(addressBook);
        return R.success("设置成功");
    }

    @GetMapping("/{id}")
    public R<AddressBook> selectOne(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("更新成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids){
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }

    @GetMapping("/default")
    public R<AddressBook> selectDefault(){
        AddressBook default_address = addressBookService.getOne(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, 1));
        return R.success(default_address);
    }
}
