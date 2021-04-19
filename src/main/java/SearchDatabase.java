
import MyDate.DateUtil;

import java.util.Date;

public class SearchDatabase  {
	private Integer Id;
	private Integer SourceId;

	private String DbCode;

	private String DbName;

	private String TableName;
	private String DbTag;
	private Integer ParentId;
	private int TableId;
	private int IsDeleted;
	private String Action;
	private String ParentCateField;
	private String ChildCateField;
	private String DetailUrlFormat;
	private Integer SortCode;
	private int IsPublish;
	private int IsSystem;
	//0：不能作为语料，1：可以作为语料
	private int isCorpus;

	public int getIsCorpus() {
		return isCorpus;
	}

	public void setIsCorpus( int isCorpus ) {
		this.isCorpus = isCorpus;
	}

	public String getDbTag() {
		return DbTag;
	}

	public void setDbTag(String dbTag) {
		DbTag = dbTag;
	}
	public int getDataType() {
		return DataType;
	}

	public void setDataType(int dataType) {
		DataType = dataType;
	}

	private int DataType;

	public Integer getSourceId() {
		return SourceId;
	}
	public void setSourceId(Integer sourceId) {
		SourceId = sourceId;
	}
	private String FullTextField;
	public String getParentCateField() {
		return ParentCateField;
	}
	public void setParentCateField(String parentCateField) {
		ParentCateField = parentCateField;
	}
	public String getChildCateField() {
		return ChildCateField;
	}
	public void setChildCateField(String childCateField) {
		ChildCateField = childCateField;
	}
	private Date CreateTime;
	private Date UpdateTime;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getDbCode() {
		return DbCode;
	}
	public void setDbCode(String dbCode) {
		DbCode = dbCode;
	}
	public String getDbName() {
		return DbName;
	}
	public void setDbName(String dbName) {
		DbName = dbName;
	}
	public String getTableName() {
		return TableName;
	}
	public void setTableName(String tableName) {
		TableName = tableName;
	}
	public int getIsDeleted() {
		return IsDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		IsDeleted = isDeleted;
	}
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	public Integer getSortCode() {
		return SortCode;
	}
	public void setSortCode(Integer sortCode) {
		SortCode = sortCode;
	}
	public String getFullTextField() {
		return FullTextField;
	}
	public void setFullTextField(String fullTextField) {
		FullTextField = fullTextField;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	public Date getUpdateTime() {
		return UpdateTime;
	}
	public String getUpdateTimeFormat() {
		return DateUtil.formatOnlyDay(UpdateTime);
	}
	public String getCreateTimeFormat() {
		return DateUtil.formatOnlyDay(UpdateTime);
	}
	public void setUpdateTime(Date updateTime) {
		UpdateTime = updateTime;
	}
	/**
	 * @return the isPublish
	 */
	public int getIsPublish() {
		return IsPublish;
	}
	public String getIsPublishFormat() {
		if(IsPublish==0){
			return "未发布";
		}
		return "已发布";
	}
	public int getIsSystem() {
		return IsSystem;
	}

	public void setIsSystem(int isSystem) {
		IsSystem = isSystem;
	}
	/**
	 * @param isPublish the isPublish to set
	 */
	public void setIsPublish(int isPublish) {
		IsPublish = isPublish;
	}
	/**
	 * @return the tableId
	 */
	public int getTableId() {
		return TableId;
	}
	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(int tableId) {
		TableId = tableId;
	}

	public Integer getParentId() {
		return ParentId;
	}

	public void setParentId(Integer parentId) {
		ParentId = parentId;
	}
	public String getDetailUrlFormat() {
		return DetailUrlFormat;
	}

	public void setDetailUrlFormat(String detailUrlFormat) {
		DetailUrlFormat = detailUrlFormat;
	}

}
