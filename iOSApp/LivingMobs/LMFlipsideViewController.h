//
//  LMFlipsideViewController.h
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LMFlipsideViewController;

@protocol LMFlipsideViewControllerDelegate
- (void)flipsideViewControllerDidFinish:(LMFlipsideViewController *)controller;
@end

@interface LMFlipsideViewController : UIViewController

@property (weak, nonatomic) id <LMFlipsideViewControllerDelegate> delegate;

- (IBAction)done:(id)sender;

@end
