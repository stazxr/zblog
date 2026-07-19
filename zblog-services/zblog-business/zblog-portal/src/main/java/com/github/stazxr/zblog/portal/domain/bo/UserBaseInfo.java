package com.github.stazxr.zblog.portal.domain.bo;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.content.ext.domain.entity.VisitorProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户基本信息
 *
 * @author SunTao
 * @since 2027-07-19
 */
@Getter
@Setter
@ApiModel("用户基本信息")
public class UserBaseInfo implements Serializable {
    private static final long serialVersionUID = -6995102083248032506L;

    public UserBaseInfo() {
    }

    public UserBaseInfo(User user) {
        if (user != null) {
            this.id = String.valueOf(user.getId());
            this.nickname = user.getNickname();
            this.avatar = user.getHeadImgUrl();
            this.gender = String.valueOf(user.getGender());
            this.webSite = user.getWebsite();
            this.intro = user.getSignature();
            this.email = user.getEmail();
        }
    }

    public UserBaseInfo(VisitorProfile visitorProfile) {
        if (visitorProfile != null) {
            this.id = visitorProfile.getVisitorId();
            this.nickname = visitorProfile.getNickname();
            this.avatar = visitorProfile.getAvatar();
        }
    }

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String id;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    private String gender;

    /**
     * 用户网站
     */
    @ApiModelProperty("用户网站")
    private String webSite;

    /**
     * 用户简介
     */
    @ApiModelProperty("用户简介")
    private String intro;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;
}
