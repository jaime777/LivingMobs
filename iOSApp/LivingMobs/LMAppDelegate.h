//
//  LMAppDelegate.h
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LMLocationTracker.h"

@interface LMAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
// location management
@property LMLocationTracker * locationTracker;
@property (nonatomic) NSTimer* locationUpdateTimer;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end
