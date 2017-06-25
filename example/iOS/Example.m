//
//  Example.m
//  RNApp
//
//  Created by 夜の乐 on 2017/6/25.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Example : NSObject
@property(nonatomic,strong)NSString* name;

-(NSString*) getName;
+(NSString*) name:(NSString*) name;
@end



@implementation Example

-(NSString *)getName{
  return self.name;
}

+(NSString *)name:(NSString *)name{
  return [NSString stringWithFormat:@"hello %@",name];
}

@end
