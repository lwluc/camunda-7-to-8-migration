package org.camunda.community.converter.visitor.impl.attribute;

import org.camunda.community.converter.BpmnDiagramCheckResult.Severity;
import org.camunda.community.converter.DomElementVisitorContext;
import org.camunda.community.converter.convertible.AbstractActivityConvertible;
import org.camunda.community.converter.expression.ExpressionTransformationResult;
import org.camunda.community.converter.expression.ExpressionTransformer;
import org.camunda.community.converter.message.Message;
import org.camunda.community.converter.message.MessageFactory;
import org.camunda.community.converter.visitor.AbstractSupportedAttributeVisitor;

public class CollectionVisitor extends AbstractSupportedAttributeVisitor {
  @Override
  public String attributeLocalName() {
    return "collection";
  }

  @Override
  protected Message visitSupportedAttribute(DomElementVisitorContext context, String attribute) {
    ExpressionTransformationResult transformationResult =
        ExpressionTransformer.transform(attribute);
    context.addConversion(
        AbstractActivityConvertible.class,
        AbstractActivityConvertible::initializeLoopCharacteristics);
    context.addConversion(
        AbstractActivityConvertible.class,
        conversion ->
            conversion
                .getBpmnMultiInstanceLoopCharacteristics()
                .getZeebeLoopCharacteristics()
                .setInputCollection(transformationResult.getNewExpression()));
    context.addMessage(Severity.TASK, MessageFactory.collectionHint());
    return MessageFactory.collection(
        attributeLocalName(), context.getElement().getLocalName(), transformationResult);
  }
}
