//
//  RCTJsc.h
//  RCTJsc
//
//  Created by 夜の乐 on 2017/6/24.
//  Copyright © 2017年 夜の乐. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTLog.h>

@interface RCTJsc : NSObject <RCTBridgeModule>
@property(nonatomic,strong)NSMutableDictionary* mObjectMap;


- (id) toJavascriptObject:(id) obj;

- (BOOL) isProxyObject:(id) obj;

- (id) invokeMethod:(Class) clazz :(id) obj :(NSString*)fun :(NSArray*) args :(BOOL) isClass;
@end
