package com.chinchinne.categoryservice.service;

import com.chinchinne.categoryservice.domain.entity.Category;
import com.chinchinne.categoryservice.domain.model.Common;
import com.chinchinne.categoryservice.domain.value.UserId;
import com.chinchinne.categoryservice.model.CategoryDto;
import com.chinchinne.categoryservice.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService
{
    ModelMapper modelMapper;

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto)
    {
        Category category = new Category
        (
             new UserId(categoryDto.getUserId())
            ,categoryDto.getName()
            ,categoryDto.getColor()
            ,Common.NO
        );

        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

//    @Transactional
//    public AccountDto changeAccount(AccountDto accountDto)
//    {
//        List<Account> accounts = accountRepository.findAll(AccountSpecs.AccountId(accountDto.getAccountId()).and(AccountSpecs.DelYn(Common.NO)))
//                                                  .orElseGet(ArrayList::new);
//
//        if( accounts.isEmpty() )
//        {
//            throw new CustomException(ErrorCode.NOT_FOUND_RECORD);
//        }
//
//        Account account = accounts.get(0);
//        account.changeAccount
//        (
//             new UserId(accountDto.getUserId())
//            ,new CategoryId(BigInteger.valueOf( Long.parseLong(accountDto.getCategory()) ))
//            ,accountDto.getStatus()
//            ,accountDto.getMemo()
//            ,accountDto.getAmount()
//        );
//
//        return modelMapper.map(account, AccountDto.class);
//    }
//
//    @Transactional
//    public AccountDto removeAccount(AccountDto accountDto)
//    {
//        List<Account> accounts = accountRepository.findAll(AccountSpecs.AccountId(accountDto.getAccountId()).and(AccountSpecs.DelYn(Common.NO)))
//                                                  .orElseGet(ArrayList::new);
//
//        if( accounts.isEmpty() )
//        {
//            throw new CustomException(ErrorCode.NOT_FOUND_RECORD);
//        }
//
//        Account account = accounts.get(0);
//        account.removeAccount
//        (
//             new UserId(accountDto.getUserId())
//            ,Common.YES
//        );
//
//        return modelMapper.map(account, AccountDto.class);
//    }

}
