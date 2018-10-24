const app = getApp()

Page({
  // 初始数据
  data: {
    url: '/custom/list/',
    onRefresh: false,
    isShow: false,
    search: "",
    dataList: [],
    hasNext: true,
    pageNum: 1,
    loading: false,
    delView: false,
    checkList: []
  },
  showInput() {
    this.setData({isShow: true})
  },
  hideInput() {
    this.setData({isShow: false})
  },
  inputTyping(e) {
    this.setData({search: e.detail.value})
  },
  // 页面加载
  onLoad(options) {
    app.queryListPage(this, 1)
  },
  // 下拉页面动作
  onPullDownRefresh() {
    this.doSearch()
    wx.stopPullDownRefresh()
  },
  // 上拉触底动作
  onReachBottom() {
    // 防止重复请求
    if (this.data.loading) return
    // 显示加载图标
    this.setData({ loading: true })
    if (this.data.hasNext) {
      app.queryListPage(this, this.data.pageNum + 1)
    }
  },
  // 搜索客户信息
  doSearch() {
    this.setData({dataList: []})
    app.queryListPage(this, 1)
  },
  // 呼叫客户手机
  callTo(e) {
    let phone = e.target.dataset.phone
    wx.showModal({
      content: '确定要呼叫 ' + phone + ' 吗？',
      cancelText: '点错',
      confirmText: '呼叫',
      success: res => {
        if (res.confirm) {
          wx.makePhoneCall({phoneNumber: phone}) //仅为示例，并非真实的电话号码
        }
      }
    })
  },
  // 编辑客户信息
  navigateTo(e) {
    if (!this.data.delView) {
      wx.navigateTo({url: '../add/add?id=' + e.currentTarget.id})
    }
  },
  // 删除工具栏
  toDelete(e) {
    this.setData({delView: true})
  },
  // 全选按钮
  checkAll(e) {
    let list = this.data.dataList
    for (let i = 0; i < list.length; i++) {
      list[i].checked = true
      this.data.checkList.push(list[i].id)
    }
    this.setData({dataList: list, checkList: this.data.checkList})
  },
  // 取消按钮
  doCancel() {
    let list = this.data.dataList
    for (let i = 0; i < list.length; i++) {
      list[i].checked = false
    }
    this.setData({
      dataList: list,
      checkList: [],
      delView: false
    })
  },
  // 删除按钮
  doDelete() {
    wx.request({
      url: app.serverIp + '/custom/delete',
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
      },
      data: JSON.stringify(this.data.checkList),
      success: res => {
        if (res.data.success) {
          this.setData({
            checkList: [],
            delView: false
          })
          this.doSearch()
        } else {
          wx.showToast({
            title: '删除失败',
            icon: 'none',
            duration: 2000
          })
        }
      }
    })
  },
  // 复选框改变
  checkboxChange(e) {
    this.setData({checkList: e.detail.value})
  }
})