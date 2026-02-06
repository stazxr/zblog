package com.github.stazxr.zblog.bas.context.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a context tag in the ZBLOG framework.
 *
 * <p>
 * Encapsulates tagName and tagValue.
 * Equality is based solely on tagName to ensure uniqueness in collections.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-22
 */
public class ContextTag implements Serializable {
    private static final long serialVersionUID = -5644282630954166219L;

    /** Name of the tag, must be unique */
    private String tagName;

    /** Value associated with the tag */
    private String tagValue;

    public ContextTag() {
    }

    /**
     * Constructs a ContextTag with tagName and tagValue.
     *
     * @param tagName  the name of the tag
     * @param tagValue the value of the tag
     */
    public ContextTag(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContextTag)) return false;
        ContextTag that = (ContextTag) o;
        return Objects.equals(tagName, that.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }

    @Override
    public String toString() {
        return tagName + "=" + tagValue;
    }
}
