//
//  LMLocationShareModel.m
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import "LMLocationShareModel.h"

@implementation LMLocationShareModel

//Class method to make sure the share model is synch across the app
+ (id)sharedModel
{
    static id sharedMyModel = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyModel = [[self alloc] init];
    });
    return sharedMyModel;
}


@end
