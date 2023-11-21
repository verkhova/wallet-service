package com.gfl.walletservice.mapper;

import com.gfl.walletservice.dto.AccountDto;
import com.gfl.walletservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    AccountDto map(Account entity);

}
