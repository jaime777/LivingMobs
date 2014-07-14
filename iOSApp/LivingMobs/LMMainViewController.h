//
//  LMMainViewController.h
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import "LMFlipsideViewController.h"

#import <CoreData/CoreData.h>

@interface LMMainViewController : UIViewController <LMFlipsideViewControllerDelegate>

@property (strong, nonatomic) NSManagedObjectContext *managedObjectContext;

@end
