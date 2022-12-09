package com.github.stazxr.zblog.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.domain.entity.Visitor;
import com.github.stazxr.zblog.mapper.VisitorAreaMapper;
import com.github.stazxr.zblog.mapper.VisitorMapper;
import com.github.stazxr.zblog.service.PortalService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台服务实现层
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Service
@RequiredArgsConstructor
public class PortalServiceImpl implements PortalService {
    private static final String UNKNOWN_AREA = "外星人";

    private static final String LOCAL_AREA_NET = "局域网";

    private final VisitorMapper visitorMapper;

    private final VisitorAreaMapper visitorAreaMapper;

    /**
     * 记录访客信息
     *
     * @param request 请求信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordVisitor(HttpServletRequest request) {
        // 获取访问信息
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = userAgent.getOperatingSystem();
        String ipAddress = IpUtils.getIp(request);

        // 生成访问唯一编码
        String uuid = ipAddress + os.getName();
        String md5Uuid = DigestUtils.md5DigestAsHex(uuid.getBytes());

        // 判断是否访问过
        String browserName = IpUtils.getBrowser(request);
        Visitor visitor = Visitor.builder().id(md5Uuid).addressIp(ipAddress).browserName(browserName).osName(os.getName()).build();
        boolean exists = visitorMapper.exists(new LambdaQueryWrapper<Visitor>().eq(Visitor::getId, md5Uuid));
        if (!exists) {
            // 第一次访问
            JSONObject cityInfo = IpImplUtils.getHttpCityDetailInfo(ipAddress);
            String province = cityInfo.get("addr", String.class);
            if (StringUtils.isBlank(province) || LOCAL_AREA_NET.equals(province.trim())) {
                updateVisitorAreaCount(UNKNOWN_AREA);
            } else {
                province = province.trim().substring(0, 2);
                visitor.setProvince(province);
                visitor.setAreaCode(cityInfo.get("cityCode", String.class));
                updateVisitorAreaCount(province);
            }

            // 保存访客信息
            visitorMapper.insert(visitor);
        }

        // 网站访问量自增
        visitorMapper.addWebVisitorCount();
    }

    private synchronized void updateVisitorAreaCount(String area) {
        String currentTime = DateUtils.formatNow();
        if (visitorAreaMapper.judgeAreaExist(area)) {
            visitorAreaMapper.updateAreaCount(area, currentTime);
        } else {
            visitorAreaMapper.insertAreaCount(area, currentTime);
        }
    }
}
