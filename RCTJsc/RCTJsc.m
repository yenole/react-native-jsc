//
//  RCTJsc.m
//  RCTJsc
//
//  Created by 夜の乐 on 2017/6/24.
//  Copyright © 2017年 夜の乐. All rights reserved.
//

#import "RCTJsc.h"

@implementation RCTJsc

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(obtain:(NSString *)name :(RCTResponseSenderBlock) callback)
{
    if (name!=nil) {
        Class clazz = NSClassFromString(name);
        id obj = [[clazz alloc] init];
        callback([NSArray arrayWithObjects:[self toJavascriptObject:obj], nil]);
    }else{
        callback(@[[NSNull null],@"Argument is null!"]);
    }
}

RCT_EXPORT_METHOD(fun:(NSDictionary*)jsObj :(NSString*) fun :(NSArray*) args :(RCTResponseSenderBlock) callback){
    if ([self isProxyObject:jsObj]) {
        id hash = [NSString stringWithFormat:@"%lu",[[jsObj objectForKey:@"hash"] unsignedIntegerValue]];
        if([self.mObjectMap.allKeys containsObject:hash]){
            id obj = [self.mObjectMap objectForKey:hash];
            fun = [self generateFun:fun :args];
            if([obj respondsToSelector:NSSelectorFromString(fun)]){
                id ret = [self invokeMethod:[obj class] :obj :fun :args :NO];
                if(ret==nil){
                    callback(@[[NSNull  null]]);
                }else{
                    callback([NSArray arrayWithObjects:[self toJavascriptObject:ret], nil]);
                }
            }else{
                callback(@[[NSNull null],@"Not found Method!"]);
            }
            return;
        }
    }
    callback(@[[NSNull null],@"Not found Object-C object!"]);
}

RCT_EXPORT_METHOD(sFun:(NSString*)name :(NSString*)fun :(NSArray*) args :(RCTResponseSenderBlock) callback){
    if(name!=nil){
        Class clazz = NSClassFromString(name);
        fun = [self generateFun:fun :args];
        id ret = [self invokeMethod:clazz :nil :fun :args :YES];
        if(ret==nil){
            callback(@[[NSNull  null]]);
        }else{
            callback([NSArray arrayWithObjects:[self toJavascriptObject:ret], nil]);
        }
    }
}

RCT_EXPORT_METHOD(release:(NSDictionary*)jsObj){
    if (self.mObjectMap!=nil && [self isProxyObject:jsObj]) {
        id hash = [NSString stringWithFormat:@"%lu",[[jsObj objectForKey:@"hash"] unsignedIntegerValue]];
        if([self.mObjectMap.allKeys containsObject:hash]){
            [self.mObjectMap removeObjectForKey:hash];
        }
    }
}

-(NSString *)generateFun:(NSString *)fun :(NSArray *)args{
    if (args!=nil) {
        for (NSUInteger i = 0; i<args.count; i++) {
            fun = [fun stringByAppendingString:@":"];
        }
    }
    return fun;
}

-(id)toJavascriptObject:(id)obj{
    if([obj isKindOfClass:[NSNumber class]] ||[obj isKindOfClass:[NSString class]]||[obj isKindOfClass:[NSArray class]]||[obj isKindOfClass:[NSDictionary class]]){
        return obj;
    }
    if (self.mObjectMap==nil) {
        self.mObjectMap = [NSMutableDictionary dictionaryWithObject:obj forKey:[NSString stringWithFormat:@"%lu", [obj hash]]];
    }else if(![self.mObjectMap.allValues containsObject:obj]){
        [self.mObjectMap setValue:obj forKey:[NSString stringWithFormat:@"%lu", [obj hash]]];
    }
    return [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithBool:YES],@"_C",[NSNumber numberWithUnsignedInteger:[obj hash]],@"hash", nil];
}

-(BOOL)isProxyObject:(id)obj{
    return obj!=nil && [[obj allKeys] containsObject:@"hash"] && [[obj allKeys] containsObject:@"_C"];
}

-(id)invokeMethod:(Class) clazz :(id)obj :(NSString *)fun :(NSArray *)args :(BOOL) isClass{
    SEL sel = NSSelectorFromString(fun);
    id signature = isClass?[clazz methodSignatureForSelector:sel]: [clazz instanceMethodSignatureForSelector:sel];
    if (signature!=nil) {
        id invocation =  [NSInvocation invocationWithMethodSignature:signature];
        [invocation setSelector:sel];
        if (args!=nil) {
            for(NSInteger i =0;i<args.count;i++){
                id arg = [args objectAtIndex:i];
                [invocation setArgument:&arg atIndex:i+2];
            }
        }
        [invocation invokeWithTarget:isClass?clazz:obj];
        if (strcmp([signature methodReturnType], "v")!=0) {
            NSObject* __weak ret;
            [invocation getReturnValue:&ret];
            return ret;
        }

    }
    return nil;
}

@end
