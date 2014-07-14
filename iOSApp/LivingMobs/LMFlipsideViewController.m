//
//  LMFlipsideViewController.m
//  LivingMobs
//
//  Created by Ricardo on 7/14/14.
//  Copyright (c) 2014 LivingMobs. All rights reserved.
//

#import "LMFlipsideViewController.h"

@interface LMFlipsideViewController ()

@end

@implementation LMFlipsideViewController

- (void)awakeFromNib
{
    [super awakeFromNib];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Actions

- (IBAction)done:(id)sender
{
    [self.delegate flipsideViewControllerDidFinish:self];
}

@end
