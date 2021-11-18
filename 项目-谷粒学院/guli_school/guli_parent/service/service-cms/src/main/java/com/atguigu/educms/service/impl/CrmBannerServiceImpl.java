package com.atguigu.educms.service.impl;

import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.mapper.CrmBannerMapper;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author gouwenyong
 * @since 2021-10-06
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    public void pageBanner(Page<CrmBanner> pageParam, Wrapper<T> queryWrapper) {
        baseMapper.selectPage(pageParam, null);
    }

    @Override
    public CrmBanner getBannerById(String id) {

        return baseMapper.selectById(id);
    }

    @Override
    public void saveBanner(CrmBanner banner) {
        baseMapper.insert(banner);
    }

    @Override
    public void updateBannerById(CrmBanner banner) {
        baseMapper.updateById(banner);
    }

    @Override
    public void removeBannerById(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = "banner" ,key = "'selectIndexList'")
    public List<CrmBanner> selectIndexList() {
        List<CrmBanner> crmBanners = baseMapper.selectList(null);

        //todo redis
        return crmBanners;
    }
}
