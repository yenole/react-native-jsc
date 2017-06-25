#react-native-jsc

[![npm version](https://badge.fury.io/js/react-native-jsc.svg)](https://badge.fury.io/js/react-native-jsc)

适用Android、iOS

## 有问题反馈
在使用中有任何问题，欢迎反馈给我，也可以用以下联系方式跟我交流

* 邮件(Netxy#vip.qq.com, 把#换成@)
* QQ: 850265689


## 添加到项目中

 1. Run `npm install react-native-jsc --save` or `yarn add react-native-jsc`
 2. react-native link react-native-jsc
 3. `var Jsc = require('react-native-jsc')`;


 ## 方法

- **`obtainPackage(package:string)`** - 这是android包名
- **`obtain(className:string ,callback:function)`** - 获取一个实例
- **`fun(jsObj:object ,fun:string ,args:Array ,callback:function)`** - 执行实例函数
- **`sFun(className:string ,fun:string ,args:Array ,callback:function)`** - 执行类函数
- **`release(jsObj:object)`** - 销毁obtain获得的实例或通过sFun|fun回调的对象

## 使用

```javascript

import Jsc from 'react-native-jsc';

class App extends Component {
	componentDidMount() {
	}
	render() {
		return (
			<View></View>
		);
	}
}
```