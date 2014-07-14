//
//  LMLocationShareModel.h
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LMBackgroundTaskManager.h"
#import <CoreLocation/CoreLocation.h>

@interface LMLocationShareModel : NSObject

@property (nonatomic) NSTimer *timer;
@property (nonatomic) LMBackgroundTaskManager * bgTask;
@property (nonatomic) NSMutableArray *myLocationArray;

+(id)sharedModel;

@end
