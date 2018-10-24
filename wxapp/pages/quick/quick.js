const app = getApp()

Page({
  // 初始数据
  data: {
    url: '/custom/quick/',
    isShow: false,
    onRefresh: false,
    isShow: false,
    search: "",
    dataList: [],
    hasNext: true,
    pageNum: 1,
    loading: false
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
    let search = options.search
    if (search) {
      this.setData({search: search})
    }
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
  // 计数器修改
  handleStepperChange({
    detail: stepper,
    target: {dataset: {componentId}}
  }) {
    const data = this.data.dataList[componentId]
    if (data.gift > 0 && data.total < stepper) {
      wx.showModal({
        content: '当前客户享受赠送优惠，是否立即赠送？',
        confirmText: '立即赠送',
        cancelText: '下次再说',
        cancelColor: 'red',
        success: res => {
          if (res.confirm) {
            // 发送请求成功回调 -1
            wx.request({
              url: app.serverIp + '/custom/updateGiftCount/' + data.id + '-' + app.sessionKey,
              method: 'GET',
              success: res => {
                if (res.data.success) {
                  --data.gift
                  this.setData({dataList: this.data.dataList})
                }
              }
            })
          } else if (res.cancel) {
            this.updateCount(data, stepper)
          }
        }
      })
    } else {
      this.updateCount(data, stepper)
    }
  },
  // 修改购买数量
  updateCount(data, stepper) {
    // ajax操作，成功回调后计数 +1
    data.total = stepper
    this.setData({dataList: this.data.dataList})
    wx.request({
      url: app.serverIp + '/custom/addBuyCount/' + data.id + '-' + app.sessionKey,
      method: 'GET',
      success: res => {
        if (!res.data.success) {
          this.setData({isShow: true})
          setTimeout(() => {
            this.setData({isShow: false})
          }, 1500)
        }
      }
    })
  }
})