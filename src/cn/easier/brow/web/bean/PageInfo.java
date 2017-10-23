package cn.easier.brow.web.bean;

public class PageInfo {

	// 总记录数
	public int count;
	// 当前第几页
	public int pagenum;
	// 每页显示数
	public int pagesize;
	//总页数
	public int pageCount;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void pageCount() {
		if (this.pagenum < 1) {
			this.pageCount = 1;
		} else {
			this.pageCount = (this.count + this.pagesize - 1) / pagesize;
		}
	}

}
