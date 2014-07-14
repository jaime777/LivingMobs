//
//  LMBackgroundTaskManager.h
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LMBackgroundTaskManager : NSObject

+(instancetype)sharedBackgroundTaskManager;

-(UIBackgroundTaskIdentifier)beginNewBackgroundTask;


@end
