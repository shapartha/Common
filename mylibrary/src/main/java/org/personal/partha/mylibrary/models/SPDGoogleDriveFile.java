package org.personal.partha.mylibrary.models;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.User;

import java.util.List;
import java.util.Map;

public class SPDGoogleDriveFile {
    File myFile;

    private Map<String, String> appProperties;
    private File.Capabilities capabilities;
    private File.ContentHints contentHints;
    private Boolean copyRequiresWriterPermission;
    private DateTime createdTime;
    private String description;
    private Boolean explicitlyTrashed;
    private String fileExtension;
    private String folderColorRgb;
    private String fullFileExtension;
    private Boolean hasAugmentedPermissions;
    private Boolean hasThumbnail;
    private String headRevisionId;
    private String iconLink;
    private String id;
    private File.ImageMediaMetadata imageMediaMetadata;


    private Boolean isAppAuthorized;


    private String kind;


    private User lastModifyingUser;


    private String md5Checksum;


    private String mimeType;


    private Boolean modifiedByMe;


    private DateTime modifiedByMeTime;


    private DateTime modifiedTime;


    private String name;


    private String originalFilename;


    private Boolean ownedByMe;


    private List<User> owners;


    private List<String> parents;


    private List<String> permissionIds;


    private List<Permission> permissions;


    private Map<String, String> properties;


    private Long quotaBytesUsed;


    private Boolean shared;


    private DateTime sharedWithMeTime;


    private User sharingUser;


    private Long size;


    private List<String> spaces;


    private Boolean starred;


    private String teamDriveId;


    private String thumbnailLink;


    private Long thumbnailVersion;


    private Boolean trashed;


    private DateTime trashedTime;


    private User trashingUser;


    private Long version;


    private File.VideoMediaMetadata videoMediaMetadata;


    private Boolean viewedByMe;


    private DateTime viewedByMeTime;


    private Boolean viewersCanCopyContent;


    private String webContentLink;


    private String webViewLink;


    private Boolean writersCanShare;

    public SPDGoogleDriveFile(File myFile) {
        this.myFile = myFile;
        this.appProperties = myFile.getAppProperties();
        this.capabilities = myFile.getCapabilities();
        this.contentHints = myFile.getContentHints();
        this.copyRequiresWriterPermission = myFile.getCopyRequiresWriterPermission();
        this.createdTime = myFile.getCreatedTime();
        this.description = myFile.getDescription();
        this.explicitlyTrashed = myFile.getExplicitlyTrashed();
        this.fileExtension = myFile.getFileExtension();
        this.folderColorRgb = myFile.getFolderColorRgb();
        this.fullFileExtension = myFile.getFullFileExtension();
        this.hasAugmentedPermissions = myFile.getHasAugmentedPermissions();
        this.hasThumbnail = myFile.getHasThumbnail();
        this.headRevisionId = myFile.getHeadRevisionId();
        this.iconLink = myFile.getIconLink();
        this.id = myFile.getId();
        this.imageMediaMetadata = myFile.getImageMediaMetadata();
        this.isAppAuthorized = myFile.getIsAppAuthorized();
        this.kind = myFile.getKind();
        this.lastModifyingUser = myFile.getLastModifyingUser();
        this.md5Checksum = myFile.getMd5Checksum();
        this.mimeType = myFile.getMimeType();
        this.modifiedByMe = myFile.getModifiedByMe();
        this.modifiedByMeTime = myFile.getModifiedByMeTime();
        this.modifiedTime = myFile.getModifiedTime();
        this.name = myFile.getName();
        this.originalFilename = myFile.getOriginalFilename();
        this.ownedByMe = myFile.getOwnedByMe();
        this.owners = myFile.getOwners();
        this.parents = myFile.getParents();
        this.permissionIds = myFile.getPermissionIds();
        this.permissions = myFile.getPermissions();
        this.properties = myFile.getProperties();
        this.quotaBytesUsed = myFile.getQuotaBytesUsed();
        this.shared = myFile.getShared();
        this.sharedWithMeTime = myFile.getSharedWithMeTime();
        this.sharingUser = myFile.getSharingUser();
        this.size = myFile.getSize();
        this.spaces = myFile.getSpaces();
        this.starred = myFile.getStarred();
        this.teamDriveId = myFile.getTeamDriveId();
        this.thumbnailLink = myFile.getThumbnailLink();
        this.thumbnailVersion = myFile.getThumbnailVersion();
        this.trashed = myFile.getTrashed();
        this.trashedTime = myFile.getTrashedTime();
        this.trashingUser = myFile.getTrashingUser();
        this.version = myFile.getVersion();
        this.videoMediaMetadata = myFile.getVideoMediaMetadata();
        this.viewedByMe = myFile.getViewedByMe();
        this.viewedByMeTime = myFile.getViewedByMeTime();
        this.viewersCanCopyContent = myFile.getViewersCanCopyContent();
        this.webContentLink = myFile.getWebContentLink();
        this.webViewLink = myFile.getWebViewLink();
        this.writersCanShare = myFile.getWritersCanShare();

    }

    public File getMyFile() {
        return myFile;
    }

    public void setMyFile(File myFile) {
        this.myFile = myFile;
    }

    public Map<String, String> getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(Map<String, String> appProperties) {
        this.appProperties = appProperties;
    }

    public File.Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(File.Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public File.ContentHints getContentHints() {
        return contentHints;
    }

    public void setContentHints(File.ContentHints contentHints) {
        this.contentHints = contentHints;
    }

    public Boolean getCopyRequiresWriterPermission() {
        return copyRequiresWriterPermission;
    }

    public void setCopyRequiresWriterPermission(Boolean copyRequiresWriterPermission) {
        this.copyRequiresWriterPermission = copyRequiresWriterPermission;
    }

    public DateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(DateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getExplicitlyTrashed() {
        return explicitlyTrashed;
    }

    public void setExplicitlyTrashed(Boolean explicitlyTrashed) {
        this.explicitlyTrashed = explicitlyTrashed;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFolderColorRgb() {
        return folderColorRgb;
    }

    public void setFolderColorRgb(String folderColorRgb) {
        this.folderColorRgb = folderColorRgb;
    }

    public String getFullFileExtension() {
        return fullFileExtension;
    }

    public void setFullFileExtension(String fullFileExtension) {
        this.fullFileExtension = fullFileExtension;
    }

    public Boolean getHasAugmentedPermissions() {
        return hasAugmentedPermissions;
    }

    public void setHasAugmentedPermissions(Boolean hasAugmentedPermissions) {
        this.hasAugmentedPermissions = hasAugmentedPermissions;
    }

    public Boolean getHasThumbnail() {
        return hasThumbnail;
    }

    public void setHasThumbnail(Boolean hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
    }

    public String getHeadRevisionId() {
        return headRevisionId;
    }

    public void setHeadRevisionId(String headRevisionId) {
        this.headRevisionId = headRevisionId;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public File.ImageMediaMetadata getImageMediaMetadata() {
        return imageMediaMetadata;
    }

    public void setImageMediaMetadata(File.ImageMediaMetadata imageMediaMetadata) {
        this.imageMediaMetadata = imageMediaMetadata;
    }

    public Boolean getAppAuthorized() {
        return isAppAuthorized;
    }

    public void setAppAuthorized(Boolean appAuthorized) {
        isAppAuthorized = appAuthorized;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public User getLastModifyingUser() {
        return lastModifyingUser;
    }

    public void setLastModifyingUser(User lastModifyingUser) {
        this.lastModifyingUser = lastModifyingUser;
    }

    public String getMd5Checksum() {
        return md5Checksum;
    }

    public void setMd5Checksum(String md5Checksum) {
        this.md5Checksum = md5Checksum;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Boolean getModifiedByMe() {
        return modifiedByMe;
    }

    public void setModifiedByMe(Boolean modifiedByMe) {
        this.modifiedByMe = modifiedByMe;
    }

    public DateTime getModifiedByMeTime() {
        return modifiedByMeTime;
    }

    public void setModifiedByMeTime(DateTime modifiedByMeTime) {
        this.modifiedByMeTime = modifiedByMeTime;
    }

    public DateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(DateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Boolean getOwnedByMe() {
        return ownedByMe;
    }

    public void setOwnedByMe(Boolean ownedByMe) {
        this.ownedByMe = ownedByMe;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public List<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<String> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Long getQuotaBytesUsed() {
        return quotaBytesUsed;
    }

    public void setQuotaBytesUsed(Long quotaBytesUsed) {
        this.quotaBytesUsed = quotaBytesUsed;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public DateTime getSharedWithMeTime() {
        return sharedWithMeTime;
    }

    public void setSharedWithMeTime(DateTime sharedWithMeTime) {
        this.sharedWithMeTime = sharedWithMeTime;
    }

    public User getSharingUser() {
        return sharingUser;
    }

    public void setSharingUser(User sharingUser) {
        this.sharingUser = sharingUser;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<String> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<String> spaces) {
        this.spaces = spaces;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public String getTeamDriveId() {
        return teamDriveId;
    }

    public void setTeamDriveId(String teamDriveId) {
        this.teamDriveId = teamDriveId;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public Long getThumbnailVersion() {
        return thumbnailVersion;
    }

    public void setThumbnailVersion(Long thumbnailVersion) {
        this.thumbnailVersion = thumbnailVersion;
    }

    public Boolean getTrashed() {
        return trashed;
    }

    public void setTrashed(Boolean trashed) {
        this.trashed = trashed;
    }

    public DateTime getTrashedTime() {
        return trashedTime;
    }

    public void setTrashedTime(DateTime trashedTime) {
        this.trashedTime = trashedTime;
    }

    public User getTrashingUser() {
        return trashingUser;
    }

    public void setTrashingUser(User trashingUser) {
        this.trashingUser = trashingUser;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public File.VideoMediaMetadata getVideoMediaMetadata() {
        return videoMediaMetadata;
    }

    public void setVideoMediaMetadata(File.VideoMediaMetadata videoMediaMetadata) {
        this.videoMediaMetadata = videoMediaMetadata;
    }

    public Boolean getViewedByMe() {
        return viewedByMe;
    }

    public void setViewedByMe(Boolean viewedByMe) {
        this.viewedByMe = viewedByMe;
    }

    public DateTime getViewedByMeTime() {
        return viewedByMeTime;
    }

    public void setViewedByMeTime(DateTime viewedByMeTime) {
        this.viewedByMeTime = viewedByMeTime;
    }

    public Boolean getViewersCanCopyContent() {
        return viewersCanCopyContent;
    }

    public void setViewersCanCopyContent(Boolean viewersCanCopyContent) {
        this.viewersCanCopyContent = viewersCanCopyContent;
    }

    public String getWebContentLink() {
        return webContentLink;
    }

    public void setWebContentLink(String webContentLink) {
        this.webContentLink = webContentLink;
    }

    public String getWebViewLink() {
        return webViewLink;
    }

    public void setWebViewLink(String webViewLink) {
        this.webViewLink = webViewLink;
    }

    public Boolean getWritersCanShare() {
        return writersCanShare;
    }

    public void setWritersCanShare(Boolean writersCanShare) {
        this.writersCanShare = writersCanShare;
    }
}
