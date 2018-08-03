/*******************************************************************************
 * Copyright (c) 2015-2018 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

//
// @author raver119@protonmail.com
//


#include "testlayers.h"
#include <NDArray.h>
#include <type_conversions.h>


using namespace nd4j;

class QuantizationTests : public testing::Test {

};

TEST_F(QuantizationTests, Basic_Test_1) {
    auto s = TypeCast::estimateQuantizedSize(10);
    ASSERT_EQ(18, s);
}

TEST_F(QuantizationTests, Basic_Test_2) {
    auto s = TypeCast::estimateQuantizedSize(1);
    ASSERT_EQ(9, s);
}

TEST_F(QuantizationTests, Compression_Test_1) {
    NDArray<float> x('c', {10});
    x.linspace(1.0f);

    auto z = new char[TypeCast::estimateQuantizedSize(x.lengthOf())];

    TypeCast::convertToQuantized<float>(nullptr, x.buffer(), x.lengthOf(), z);

    delete z;
}