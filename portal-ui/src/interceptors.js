import HeyUI from 'heyui';
// 注册AXIOS的拦截器，这里跟路由没有关系，只是正好在这里配置路由的进度条，所以一并在这里配置AXIOS的进度条
import axios from "axios";
export default {
    addAxiosInterceptors(router) {
        // 添加一个请求拦截器，用于在发送请求之前修改请求相关的参数
        axios.interceptors.request.use(config => {
            // console.log("开始")
            HeyUI.$LoadingBar.start();
            // 可以根据实际的要求、需求修改请求参数
            return config;
        }, (error) => {
            // 当出现问题的时候，调用此函数
            // 一旦调用Promise.reject表示访问失败、结束后续的请求啊哦做
            return Promise.reject(error);
        });

        // 响应拦截器，用于处理响应的结果，比如401
        axios.interceptors.response.use(response => {
            HeyUI.$LoadingBar.success();
            // console.log("结束")
            // 正常的返回，直接把响应返回即可
            return response;
        }, (error) => {
            HeyUI.$LoadingBar.fail();
            // 错误信息返回
            if (error.response.status === 401) {
                if (error.request.responseURL.endsWith("/do-login")) {
                    // 本身就是登录页面，不处理
                } else {
                    // 未登录
                    // 401需要服务器配合，在未登录的时候返回401错误码
                    router.push("/login");
                }
            } else {
                //router.push("/error");
                console.error(error)
                HeyUI.$Message({ type: "error", text: "出现了未知问题" });
            }
            return Promise.reject(error);
        });
    }
}